package com.dev.iccaka.intervaltimer.Interfaces;

import java.io.IOException;
import java.util.List;

public interface IDataReader<T> {

    List<T> readData() throws IOException;
}
