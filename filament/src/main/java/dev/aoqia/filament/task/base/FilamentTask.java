package net.aoqia.filament.task.base;

import javax.inject.Inject;

import net.aoqia.filament.FilamentExtension;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Internal;

public abstract class FilamentTask extends DefaultTask {
    @Inject
    public FilamentTask() {
        setGroup("filament");
    }

    @Internal
    protected FilamentExtension getExtension() {
        return FilamentExtension.get(getProject());
    }
}
