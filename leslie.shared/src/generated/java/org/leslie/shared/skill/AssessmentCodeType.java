package org.leslie.shared.skill;

import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.util.ColorUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

public class AssessmentCodeType extends AbstractCodeType<Long, Double> {

	private static final long serialVersionUID = 1L;
	public static final Long ID = 200L;

	@Override
	public Long getId() {
		return ID;
	}

	@Order(1000)
	public static class VeryLowCode extends AbstractCode<Double> {
		private static final long serialVersionUID = 1L;
		public static final Double ID = 0.0;

		@Override
		protected String getConfiguredText() {
			return TEXTS.get("VeryLow");
		}

		@Override
		public Double getId() {
			return ID;
		}

		@Override
		protected String getConfiguredBackgroundColor() {
			return ColorUtility.RED;
		}
	}

	@Order(2000)
	public static class LowCode extends AbstractCode<Double> {
		private static final long serialVersionUID = 1L;
		public static final Double ID = 0.25;

		@Override
		protected String getConfiguredText() {
			return TEXTS.get("Low");
		}

		@Override
		public Double getId() {
			return ID;
		}

		@Override
		protected String getConfiguredBackgroundColor() {
			return "ff9900";// ColorUtility.rgbToText(255, 128, 0);
		}
	}

	@Order(3000)
	public static class MediumCode extends AbstractCode<Double> {
		private static final long serialVersionUID = 1L;
		public static final Double ID = 0.5;

		@Override
		protected String getConfiguredText() {
			return TEXTS.get("Medium");
		}

		@Override
		public Double getId() {
			return ID;
		}

		@Override
		protected String getConfiguredBackgroundColor() {
			return ColorUtility.YELLOW;
		}
	}

	@Order(4000)
	public static class HighCode extends AbstractCode<Double> {
		private static final long serialVersionUID = 1L;
		public static final Double ID = 0.75;

		@Override
		protected String getConfiguredText() {
			return TEXTS.get("High");
		}

		@Override
		public Double getId() {
			return ID;
		}

		@Override
		protected String getConfiguredBackgroundColor() {
			return "99ff00";// ColorUtility.rgbToText(128, 255, 0);
		}
	}

	@Order(5000)
	public static class VeryHighCode extends AbstractCode<Double> {
		private static final long serialVersionUID = 1L;
		public static final Double ID = 1.0;

		@Override
		protected String getConfiguredText() {
			return TEXTS.get("VeryHigh");
		}

		@Override
		public Double getId() {
			return ID;
		}

		@Override
		protected String getConfiguredBackgroundColor() {
			return ColorUtility.GREEN;
		}
	}

}
