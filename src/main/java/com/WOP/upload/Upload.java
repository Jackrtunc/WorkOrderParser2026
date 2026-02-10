package com.WOP.upload;

import com.WOP.fileconverters.FileConverter;

import java.io.File;
import java.io.IOException;

public class Upload {
  private final File file;
  private UploadType uploadType;
  private final FileConverter converter;

  public Upload(File file, UploadType uploadType, FileConverter converter) {
    this.file = file;
    this.uploadType = uploadType;
    this.converter = converter;
  }

  // Uploads start as undefined until a user selects their type
  public Upload(File file, FileConverter converter) {
    this(file, UploadType.UNDEFINED, converter);
  }

  public String getName() {
    return file.getName();
  }

  public File asCSV() throws IOException {
    return converter.convert(file);
  }

  public UploadType getUploadType() {
    return uploadType;
  }

  public void setUploadType(UploadType uploadType) {
    this.uploadType = uploadType;
  }
}
