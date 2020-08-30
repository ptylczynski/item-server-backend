package cloud.ptl.itemserver.error.resolver.transformers;

// F - from
// T - to

public abstract class AbstractTransformer<F, T> {
    public abstract T transform(F value);
}
