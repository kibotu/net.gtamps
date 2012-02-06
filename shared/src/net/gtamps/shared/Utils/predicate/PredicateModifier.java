package net.gtamps.shared.Utils.predicate;

public abstract class PredicateModifier<T> implements IPredicateModifier<T> {


	@SuppressWarnings({"rawtypes", "unchecked"})
	static final IPredicateModifier NOT = new PredicateModifier() {
		@Override
		public Predicate applyTo(final Predicate... subjects) {
			assert subjects.length == 1 : "expects exactly one argument";
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
	static final IPredicateModifier OR = new PredicateModifier() {
		@Override
		public Predicate applyTo(final Predicate... subjects) {
			if (subjects.length < 1) {
				throw new IllegalArgumentException("must give at least one argument");
			}
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
	static final IPredicateModifier AND = new PredicateModifier() {
		@Override
		public Predicate applyTo(final Predicate... subjects) {
			if (subjects.length < 1) {
				throw new IllegalArgumentException("must give at least one argument");
			}
			final String sigString = buildSignatureString("and", subjects);
			return new Predicate() {
				@Override
				public boolean appliesTo(final Object x) {
					for (final Predicate p : subjects) {
						if (p.appliesTo(x)) {
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
	static final IPredicateModifier XOR = new PredicateModifier() {
		@Override
		public Predicate applyTo(final Predicate... subjects) {
			if (subjects.length < 1) {
				throw new IllegalArgumentException("must give at least one argument");
			}
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
	private static final String buildSignatureString(final String name, final Predicate... subjects) {
		final StringBuilder string = new StringBuilder(String.format("Predicate %s( ", name));
		for (final Predicate p : subjects) {
			string.append(String.format("(%s) ", p.toString()));
		}
		string.append(")");
		return string.toString();
	}

}
