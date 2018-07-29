package com.dev.iccaka.intervaltimer.Interfaces;

import java.io.IOException;
import java.util.List;


public interface IDataWriter<T> {

    void addData(List<T> dataList);

    void writeData() throws IOException;

}
