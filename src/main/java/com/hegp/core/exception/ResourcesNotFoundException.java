package com.hegp.core.exception;

import java.util.function.Supplier;

public class ResourcesNotFoundException extends RuntimeException implements Supplier<ResourcesNotFoundException> {

    public ResourcesNotFoundException() {
        super("该记录不存在或者已删除");
    }

    public ResourcesNotFoundException(String message) {
        super(message);
    }

    @Override
    public ResourcesNotFoundException get() {
        return this;
    }
}