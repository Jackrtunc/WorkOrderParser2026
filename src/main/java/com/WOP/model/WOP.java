package com.WOP.model;

import com.WOP.Config;
import com.WOP.fileconverters.FileConverter;
import com.WOP.upload.Upload;
import com.WOP.upload.UploadType;
import com.WOP.view.ModelObserver;
import com.WOP.view.help.HelpPageConfig;
import com.WOP.workorder.WorkOrder;
import com.WOP.workorder.factories.DepartmentWorkOrderFactory;
import com.WOP.workorder.factories.FacilitiesWorkOrderFactory;
import com.WOP.workorder.parser.CsvWorkOrderParser;
import com.WOP.workorder.parser.Parser;
import com.WOP.workorder.storage.WorkOrderStorage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WOP implements Model {
  private Page activePage;
  private final List<Upload> uploads;
  private File outputDir;
  private final WorkOrderStorage workOrders;
  private final int MAX_FACILITIES_COUNT;
  private final int MAX_DEPARTMENT_COUNT;
  private int facilitiesCount; // Keeps track of the type of each upload
  private int departmentCount;
  private final List<ModelObserver> observers; // The view is a model observer
  private final Map<String, FileConverter> fileConverterMap;
  private final Config config;

  public WOP(
      Page activePage,
      List<Upload> uploads,
      WorkOrderStorage workOrders,
      int MAX_FACILITIES_COUNT,
      int MAX_DEPARTMENT_COUNT,
      List<ModelObserver> observers,
      Map<String, FileConverter> fileConverterMap)
      throws IOException {
    this.activePage = activePage;
    this.uploads = uploads;
    this.outputDir = null;
    this.workOrders = workOrders;
    this.MAX_FACILITIES_COUNT = MAX_FACILITIES_COUNT;
    this.MAX_DEPARTMENT_COUNT = MAX_DEPARTMENT_COUNT;
    this.facilitiesCount = 0;
    this.departmentCount = 0;
    this.observers = observers;
    this.fileConverterMap = fileConverterMap;
    this.config = loadParsingConfig(); // loads config from src/main/resources/config.json
  }

  @Override
  public List<Upload> getUploads() {
    return uploads;
  }

  @Override
  public Page getActivePage() {
    return activePage;
  }

  @Override
  public String getOutputDirPath() {
    return outputDir == null ? null : outputDir.getPath();
  }

  @Override
  public Set<String> getSupportedFileTypes() {
    return fileConverterMap.keySet();
  }

  @Override
  public HelpPageConfig getHelpPageConfig() {
    return this.config.helpPageConfig();
  }

  @Override
  public void setUploadType(Upload upload, UploadType uploadType) {
    handleUploadTypeTransition(
        upload.getUploadType(), // from
        uploadType, // to
        () -> {
          upload.setUploadType(uploadType);
          updateObservers();
        }); // set the new type iff the model state allows it
  }

  @Override
  public void setActivePage(Page activePage) {
    this.activePage = activePage;
    updateObservers();
  }

  @Override
  public void setOutputDir(File outputDir) throws IOException {
    if (outputDir == null) throw new IOException("Save location is invalid");
    this.outputDir = outputDir;
    updateObservers();
  }

  @Override
  public void addUpload(File file) throws IOException, ModelException {
    if (file == null) throw new IOException("Error uploading file");

    int MAX_UPLOAD_COUNT = MAX_DEPARTMENT_COUNT + MAX_FACILITIES_COUNT;
    if (uploads.size() >= MAX_UPLOAD_COUNT) throw new ModelException("Maximum uploads reached");

    FileConverter converter =
        fileConverterMap.get(
            getExtension(file)); // Match the file type with its corresponding converter
    if (converter == null)
      throw new ModelException("Unsupported file-type"); // .get() returns null if there's no match

    uploads.add(new Upload(file, converter)); // Store uploads with their converter
    updateObservers();
  }

  @Override
  public void removeUpload(Upload upload) {
    uploads.remove(upload);
    switch (upload.getUploadType()) {
      case DEPARTMENT:
        departmentCount--;
        break;
      case FACILITIES:
        facilitiesCount--;
        break;
    }
    updateObservers();
  }

  @Override
  public void clearUploads() {
    uploads.clear();
    facilitiesCount = 0;
    departmentCount = 0;
    updateObservers();
  }

  @Override
  public void addObserver(ModelObserver observer) {
    observers.add(observer);
  }

  @Override
  public String execute() throws Exception {
    // throws an exception if execution parameters aren't met
    validateExecution();

    // process department spreadsheet first
    for (Upload upload : uploads) {
      if (upload.getUploadType() == UploadType.DEPARTMENT) {
        try (Parser departmentParser =
            new CsvWorkOrderParser(
                upload.getName(),
                new CSVReader(
                    new FileReader(upload.asCSV())), // Convert upload to csv, pass it into a reader
                new DepartmentWorkOrderFactory(config.departmentConfig()))) {
          for (WorkOrder workOrder : departmentParser) { // Get parsed work orders one at a time
            workOrders.add(workOrder);
          }
        }
      }
    }

    // stores messages returned from work order additions
    List<String> updates = new ArrayList<>();

    // process facilities spreadsheets
    for (Upload upload : uploads) {
      if (upload.getUploadType() == UploadType.FACILITIES) {
        try (Parser facilitiesParser =
            new CsvWorkOrderParser(
                upload.getName(),
                new CSVReader(new FileReader(upload.asCSV())),
                new FacilitiesWorkOrderFactory(
                    config.facilitiesConfig()))) { // Note the different factory
          for (WorkOrder workOrder : facilitiesParser) {
            updates.add(workOrders.add(workOrder));
          }
        }
      }
    }

    // Write processed work orders to the output file (always .csv)
    File outputFile = getOutputFile();
    try (CSVWriter writer = new CSVWriter(new FileWriter(outputFile))) {
      writer.writeNext(config.departmentConfig().getHeaderNames()); // Write the column names first
      for (WorkOrder workOrder : workOrders) {
        writer.writeNext(workOrder.toArray()); // Convert work orders to arrays (csv rows)
      }
    }

    StringBuilder successMessage = new StringBuilder(String.format("Output saved at %s\n", outputFile.getAbsolutePath()));
    for (String update : updates) {
      successMessage.append(update);
      successMessage.append('\n');
    }
    return successMessage.toString();
  }

  private File getOutputFile() {
    return new File(
        outputDir,
        String.format(
            "WOP%d.csv",
            Math.abs(LocalDateTime.now().hashCode()))); // Ensures output names are different
  }

  private String getExtension(File file) {
    int lastIndex = file.getName().lastIndexOf(".");
    if (lastIndex < 0) return null;
    return file.getName().substring(lastIndex + 1);
  }

  private void validateExecution() {
    if (outputDir == null) throw new ModelException("Output location not selected");
    if (departmentCount < 1)
      throw new ModelException(
          "Department spreadsheet not selected"); // There needs to be at least 1 department and 1
    // facilities spreadsheet
    if (facilitiesCount < 1) throw new ModelException("Facilities spreadsheet not selected");
  }

  // Refreshes the view when the model's state changes
  private void updateObservers() {
    for (ModelObserver observer : observers) {
      observer.update();
    }
  }

  // Routes necessary model state updates
  private void handleUploadTypeTransition(UploadType from, UploadType to, Runnable transition) {
    if (transition == null) throw new IllegalStateException("Transition lambda was null");

    if (from == UploadType.DEPARTMENT && to == UploadType.FACILITIES) {
      if (facilitiesCount < MAX_FACILITIES_COUNT) {
        facilitiesCount++; // going from department to facilities (+1 department, -1 facilities)
        departmentCount--;
        transition.run(); // only make the change if we haven't reached the max
      }
    } else if (from == UploadType.DEPARTMENT && to == UploadType.UNDEFINED) {
      departmentCount--;
      transition.run();
    } else if (from == UploadType.FACILITIES && to == UploadType.DEPARTMENT) {
      if (departmentCount < MAX_DEPARTMENT_COUNT) {
        departmentCount++;
        facilitiesCount--;
        transition.run();
      }
    } else if (from == UploadType.FACILITIES && to == UploadType.UNDEFINED) {
      facilitiesCount--;
      transition.run();
    } else if (from == UploadType.UNDEFINED && to == UploadType.DEPARTMENT) {
      if (departmentCount < MAX_DEPARTMENT_COUNT) {
        departmentCount++;
        transition.run();
      }
    } else if (from == UploadType.UNDEFINED && to == UploadType.FACILITIES) {
      if (facilitiesCount < MAX_FACILITIES_COUNT) {
        facilitiesCount++;
        transition.run();
      }
    }
  }

  private Config loadParsingConfig() throws IOException {
    try (InputStream stream = getClass().getClassLoader().getResourceAsStream("config.json")) {

      if (stream == null) {
        throw new IOException("config.json not found");
      }

      ObjectMapper mapper =
          new ObjectMapper(); // Maps JSON objects from src/main/resources/config.json to the root
      // Config record
      return mapper.readValue(stream, Config.class);
    }
  }
}
