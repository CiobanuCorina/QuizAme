package com.company.Iterator;

import javax.naming.SizeLimitExceededException;
import java.util.Map;

public interface Iterator {
    Map.Entry<String, Boolean> getNext() throws SizeLimitExceededException;
    boolean hasMore();
}
