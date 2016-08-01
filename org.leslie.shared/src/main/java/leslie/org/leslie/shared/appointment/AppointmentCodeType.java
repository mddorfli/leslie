package leslie.org.leslie.shared.appointment;

import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

public class AppointmentCodeType extends AbstractCodeType<Long, Long> {

	private static final long serialVersionUID = 1L;

	public static final long ID = 100;

	@Override
	public Long getId() {
		return ID;
	}

	@Order(1000)
	public static class VacationCode extends AbstractCode<Long> {

		private static final long serialVersionUID = 1L;

		public static final long ID = 101;

		@Override
		protected String getConfiguredText() {
			return TEXTS.get("Vacation");
		}

		@Override
		public Long getId() {
			return ID;
		}
	}

}
