package net.gtamps.shared.Utils.predicate;


/**
 * common {@link Predicate} compositions
 *
 * @author Jan Rabe, Tom Wallroth, Til Boerner
 *
 */
class PredicateModifiers {


	@SuppressWarnings({"rawtypes", "unchecked"})
	static final IPredicateModifier NOT = new IPredicateModifier() {
		@Override
		public Predicate applyTo(final Predicate... subjects) {
			validateVarargs(1, 1, false, subjects);
			final String sigString = buildSignatureString("not", subjects);
			return new Predicate() {
				@Override
				public boolean appliesTo(final Object x) {
					return !subjects[0].appliesTo(x);
				}

				@Override
				public String toString() {
					return sigString;
				}
			};
		}
	};

	@SuppressWarnings({"rawtypes", "unchecked"})
	static final IPredicateModifier OR = new IPredicateModifier() {
		@Override
		public Predicate applyTo(final Predicate... subjects) {
			validateVarargs(1, Integer.MAX_VALUE, false, subjects);
			final String sigString = buildSignatureString("or", subjects);
			return new Predicate() {
				@Override
				public boolean appliesTo(final Object x) {
					for (final Predicate p : subjects) {
						if (p.appliesTo(x)) {
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
	static final IPredicateModifier AND = new IPredicateModifier() {
		@Override
		public Predicate applyTo(final Predicate... subjects) {
			validateVarargs(1, Integer.MAX_VALUE, false, subjects);
			final String sigString = buildSignatureString("and", subjects);
			return new Predicate() {
				@Override
				public boolean appliesTo(final Object x) {
					for (final Predicate p : subjects) {
						if (!p.appliesTo(x)) {
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

	@SuppressWarnings({"rawtypes", "unchecked"})
	static final IPredicateModifier XOR = new IPredicateModifier() {
		@Override
		public Predicate applyTo(final Predicate... subjects) {
			validateVarargs(2, Integer.MAX_VALUE, false, subjects);
			final String sigString = buildSignatureString("xor", subjects);
			return new Predicate() {
				@Override
				public boolean appliesTo(final Object x) {
					int sum = 0;
					for (final Predicate p : subjects) {
						if (p.appliesTo(x)) {
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
	protected static final String buildSignatureString(final String name, final Predicate... subjects) {
		final StringBuilder string = new StringBuilder(String.format("Predicate %s( ", name));
		for (final Predicate p : subjects) {
			string.append(String.format("(%s) ", p.toString()));
		}
		string.append(")");
		return string.toString();
	}

	private static <T> void validateVarargs(final Integer lowerBound, final Integer upperBound, final boolean allowNull, final T... varargs) throws IllegalArgumentException {
		if (varargs.length < lowerBound) {
			throw new IllegalArgumentException("must provide at least " +  lowerBound + (lowerBound == 1 ? "argument" : "arguments"));
		}
		if (varargs.length > upperBound) {
			throw new IllegalArgumentException("must provide at most " +  upperBound + (upperBound == 1 ? "argument" : "arguments"));
		}
		if (!allowNull) {
			for (final Object arg: varargs) {
				if (arg == null) {
					throw new IllegalArgumentException("null arguments not allowed");
				}
			}
		}
	}

}
