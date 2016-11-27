package com.picky.core.api;

import com.picky.core.model.DependencyMap;

import java.io.File;
import java.util.Collection;
import java.util.List;

public interface DependencyTracker {

    DependencyMap loadPreviousDependencies();

    void computeNewHashes(DependencyMap dependencies);

    void saveHashes(DependencyMap dependencies);

}
