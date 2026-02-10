package com.WOP.controller;

import com.WOP.model.Model;
import com.WOP.model.ModelException;
import com.WOP.model.Page;
import com.WOP.upload.Upload;
import com.WOP.upload.UploadType;
import java.io.File;
import java.io.IOException;

public class WOPController implements Controller {
  private final Model EPS;

  public WOPController(Model EPS) {
    this.EPS = EPS;
  }

  @Override
  public void clickHome() {
    EPS.setActivePage(Page.HOME);
  }

  @Override
  public void clickHelp() {
    EPS.setActivePage(Page.HELP);
  }

  @Override
  public void selectOutputLocation(File outputDir) throws IOException {
    EPS.setOutputDir(outputDir);
  }

  @Override
  public void addUpload(File file) throws ModelException, IOException {
    EPS.addUpload(file);
  }

  @Override
  public void removeUpload(Upload upload) {
    EPS.removeUpload(upload);
  }

  @Override
  public void setUploadType(Upload upload, UploadType uploadType) {
    EPS.setUploadType(upload, uploadType);
  }

  @Override
  public String execute() throws Exception {
    return EPS.execute();
  }

  @Override
  public void clearUploads() {
    EPS.clearUploads();
  }
}
