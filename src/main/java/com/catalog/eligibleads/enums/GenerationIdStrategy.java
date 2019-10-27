package com.catalog.eligibleads.enums;

public enum GenerationIdStrategy {
	ONLY_EVEN_NUMBER {
		@Override
		public Long defineId(long value) {
			if ((value % 2) != 0) {
				value++;
			}
			return value;
		}
	},

	ONLY_ODD_NUMBER {
		@Override
		public Long defineId(long value) {
			if ((value % 2) == 0) {
				value++;
			}
			return value;
		}
	},
	ALL_NUMBERS {
		@Override
		public Long defineId(long value) {
			return value;
		}
	};

	public abstract Long defineId(long value);

}
