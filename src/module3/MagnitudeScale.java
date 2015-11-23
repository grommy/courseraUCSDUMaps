package module3;


public enum MagnitudeScale {
    MINOR {
        public int getColor() {
            return -16776961;  //blue
        }
        public int getSize() {
            return 5;
        }
        public String getDescription() {
            return "Below 4.0";
        }
    },
    MEDIUM {
        public int getColor() {
            return -256;  //yellow
        }
        public int getSize() {
            return 10;
        }
        public String getDescription() {
            return "4.0+ magnitude";
        }
    },
    MAJOR {
        public int getColor() {
            return -65536;  //red
        }
        public int getSize() {
            return 20;
        }
        public String getDescription() {
            return "5.0+ magnitude";
        }

    };

    public abstract int getColor();
    public abstract int getSize();
    public abstract String getDescription();
}
