package com.company.Iterator;

public interface IteratorFactory<T>{
    Iterator getIterator(T t);
}
