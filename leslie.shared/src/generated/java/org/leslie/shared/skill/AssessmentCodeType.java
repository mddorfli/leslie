package org.leslie.shared.skill;

import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.util.ColorUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

public class AssessmentCodeType extends AbstractCodeType<Long, Integer> {

	private static final long serialVersionUID = 1L;
	public static final Long ID = 200L;

	@Override
	public Long getId() {
		return ID;
	}

	@Order(1000)
	public static class VeryHighCode extends AbstractCode<Integer> {
		private static final long serialVersionUID = 1L;
		public static final Integer ID = 100;

		@Override
		protected String getConfiguredText() {
			return TEXTS.get("VeryHigh");
		}

		@Override
		public Integer getId() {
			return ID;
		}

		@Override
		protected String getConfiguredForegroundColor() {
			return ColorUtility.GREEN;
		}
	}

	@Order(2000)
	public static class HighCode extends AbstractCode<Integer> {
		private static final long serialVersionUID = 1L;
		public static final Integer ID = 75;

		@Override
		protected String getConfiguredText() {
			return TEXTS.get("High");
		}

		@Override
		public Integer getId() {
			return ID;
		}

		@Override
		protected String getConfiguredForegroundColor() {
			return "99ff00";
		}
	}

	@Order(3000)
	public static class MediumCode extends AbstractCode<Integer> {
		private static final long serialVersionUID = 1L;
		public static final Integer ID = 50;

		@Override
		protected String getConfiguredText() {
			return TEXTS.get("Medium");
		}

		@Override
		public Integer getId() {
			return ID;
		}

		@Override
		protected String getConfiguredForegroundColor() {
			return ColorUtility.YELLOW;
		}
	}

	@Order(4000)
	public static class LowCode extends AbstractCode<Integer> {
		private static final long serialVersionUID = 1L;
		public static final Integer ID = 25;

		@Override
		protected String getConfiguredText() {
			return TEXTS.get("Low");
		}

		@Override
		public Integer getId() {
			return ID;
		}

		@Override
		protected String getConfiguredForegroundColor() {
			return "ff9900";
		}
	}

	@Order(5000)
	public static class VeryLowCode extends AbstractCode<Integer> {
		private static final long serialVersionUID = 1L;
		public static final Integer ID = 0;

		@Override
		protected String getConfiguredText() {
			return TEXTS.get("VeryLow");
		}

		@Override
		public Integer getId() {
			return ID;
		}

		@Override
		protected String getConfiguredForegroundColor() {
			return ColorUtility.RED;
		}
	}

}
