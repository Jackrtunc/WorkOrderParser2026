package com.WOP.fileconverters;

import java.io.File;

public class CSVConverter implements FileConverter {
  @Override
  public File convert(File input) {
    // no conversion necessary
    return input;
  }
}
