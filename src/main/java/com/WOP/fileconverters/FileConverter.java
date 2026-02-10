package com.WOP.fileconverters;

import java.io.File;
import java.io.IOException;

public interface FileConverter {
  // All converters convert to csv to make processing easier
  File convert(File input) throws IOException;
}
