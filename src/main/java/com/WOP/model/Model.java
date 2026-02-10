package com.WOP.model;

import com.WOP.upload.Upload;
import com.WOP.upload.UploadType;
import com.WOP.view.ModelObserver;
import com.WOP.view.help.HelpPageConfig;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface Model {

  void setUploadType(Upload upload, UploadType uploadType);

  List<Upload> getUploads(); // evaluate safety

  Page getActivePage();

  void setActivePage(Page activePage);

  String getOutputDirPath();

  void setOutputDir(File outputDir) throws IOException;

  Set<String> getSupportedFileTypes();

  void addUpload(File upload) throws IOException;

  void removeUpload(Upload upload);

  void addObserver(ModelObserver observer);

  String execute() throws Exception;

  void clearUploads();

  HelpPageConfig getHelpPageConfig();
}
