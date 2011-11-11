package net.gtamps.shared.Utils.predicate;

public abstract class PredicateModifier<T> implements IPredicateModifier<T> {

    @SuppressWarnings("unchecked")
    public static final <T> Predicate<T> not(final Predicate<T> p) {
        return NOT.applyTo(p);
    }

    @SuppressWarnings("unchecked")
    public static final <T> Predicate<T> and(final Predicate<T>... p) {
        return AND.applyTo(p);
    }

    @SuppressWarnings("unchecked")
    public static final <T> Predicate<T> or(final Predicate<T>... p) {
        return OR.applyTo(p);
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    private static final IPredicateModifier NOT = new PredicateModifier() {
        @Override
        public Predicate applyTo(final Predicate... subjects) {
            assert subjects.length == 1 : "expects exactly one argument";
            final String sigString = buildSignatureString("not", subjects);
            return new Predicate() {
                @Override
                public boolean isTrueFor(final Object x) {
                    return !subjects[0].isTrueFor(x);
                }

                @Override
                public String toString() {
                    return sigString;
                }
            };
        }
    };

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static final IPredicateModifier OR = new PredicateModifier() {
        @Override
        public Predicate applyTo(final Predicate... subjects) {
            if (subjects.length < 1) {
                throw new IllegalArgumentException("must give at least one argument");
            }
            final String sigString = buildSignatureString("or", subjects);
            return new Predicate() {
                @Override
                public boolean isTrueFor(final Object x) {
                    for (final Predicate p : subjects) {
                        if (p.isTrueFor(x)) {
                            return true;
                        }
                    }
                    return false;
                }

                @Override
                public String toString() {
                    return sigString;
                }
            };
        }
    };

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static final IPredicateModifier AND = new PredicateModifier() {
        @Override
        public Predicate applyTo(final Predicate... subjects) {
            if (subjects.length < 1) {
                throw new IllegalArgumentException("must give at least one argument");
            }
            final String sigString = buildSignatureString("and", subjects);
            return new Predicate() {
                @Override
                public boolean isTrueFor(final Object x) {
                    for (final Predicate p : subjects) {
                        if (p.isTrueFor(x)) {
                            return false;
                        }
                    }
                    return true;
                }

                @Override
                public String toString() {
                    return sigString;
                }
            };
        }
    };

    @SuppressWarnings({"rawtypes", "unchecked", "unused"})
    private static final IPredicateModifier XOR = new PredicateModifier() {
        @Override
        public Predicate applyTo(final Predicate... subjects) {
            if (subjects.length < 1) {
                throw new IllegalArgumentException("must give at least one argument");
            }
            final String sigString = buildSignatureString("xor", subjects);
            return new Predicate() {
                @Override
                public boolean isTrueFor(final Object x) {
                    int sum = 0;
                    for (final Predicate p : subjects) {
                        if (p.isTrueFor(x)) {
                            sum++;
                        }
                    }
                    return sum == 1;
                }

                @Override
                public String toString() {
                    return sigString;
                }
            };
        }
    };

    @SuppressWarnings("rawtypes")
    private static final String buildSignatureString(final String name, final Predicate... subjects) {
        final StringBuilder string = new StringBuilder(String.format("Predicate %s( ", name));
        for (final Predicate p : subjects) {
            string.append(String.format("(%s) ", p.toString()));
        }
        string.append(")");
        return string.toString();
    }

}
