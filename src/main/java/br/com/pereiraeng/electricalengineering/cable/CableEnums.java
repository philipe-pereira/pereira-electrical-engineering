package br.com.pereiraeng.electricalengineering.cable;

public class CableEnums {

	public enum CondMat {
		COBRE, ALUMINIO;

		public String getShort() {
			switch (this) {
			case COBRE:
				return "Cu";
			case ALUMINIO:
				return "Al";
			default:
				return null;
			}
		}
	}

	public enum IsoMat {
		PVC, PE, XLPE, TR_XLPE, EPR, HEPR, EPR_105;
	}

	public enum CobMat {
		ST1, ST2, ST3, ST7;
	}

	public enum ClassEncord {
		CE1, CE2, CE5, CE6;
	}

	public enum Secoes {
		S0_5, S0_75, S1, S1_5, S2_5, S4, S6, S10, S16, S25, S35, S50, S70, S95, S120, S150, S185, S240, S300, S400, S500, S630, S800, S1000, S1200, S1600, S2000;

		public String toString() {
			String out = this.name();
			return out.substring(1, out.length()).replace('_', '.') + " mm\u00B2";
		}

		public float bitola() {
			return Float.parseFloat(toString());
		}
	}
}
