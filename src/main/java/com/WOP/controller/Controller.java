package com.WOP.controller;

import com.WOP.model.ModelException;
import com.WOP.upload.Upload;
import com.WOP.upload.UploadType;
import java.io.File;
import java.io.IOException;

public interface Controller {
  void clickHome();

  void clickHelp();

  void selectOutputLocation(File output) throws IOException;

  void addUpload(File file) throws IOException, ModelException;

  void removeUpload(Upload upload);

  void setUploadType(Upload upload, UploadType uploadType);

  String execute() throws Exception;

  void clearUploads();
}
