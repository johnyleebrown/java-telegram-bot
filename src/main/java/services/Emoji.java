package services;

public enum Emoji {

    ROCKET('\uD83D', '\uDE80'),
    MOVIECAMERA('\uD83D','\uDCFD'),
    MOVIETIME('\uD83C','\uDFAC'),
    FINISH('\uD83C','\uDFC1'),
    CASH('\uD83D','\uDCB0'),
    CURRENTLOC('\uD83D','\uDCCD'),
    CAMERAEM('\uD83C','\uDFA6'),
    NEWTAG('\uD83C','\uDD95'),
    LEFTARROW(null,'\u2B05'),
    WHITEFLAG('\uD83C','\uDFF3'),
    BLACKFLAG('\uD83C','\uDFF4'),
    LOWEST('\uD83D','\uDD3D'),
    STORED('\uD83D','\uDCBE'),
    PENENTERLOC('\uD83D','\uDD8C'),
    SAVEDLOC('\uD83D','\uDCC2'),
    HEAVYPLUS(null,'\u2795'),
    SETT(null,'\u2699'),
    DOWN(null,'\uFE0F'),
    OK('\uD83D','\uDC4C'),
    CELEB('\uD83C', '\uDF89'),
    FINGDOWN('\uD83D','\uDC47'),
    RANGE(null,'\u2194'),
    HIT('\uD83C', '\uDFAF'),
    LEFTTRIANGLE(null,'\u25C0'),
    CHANGE('\uD83D','\uDD04'),
    CASTLE('\uD83C','\uDFF0'),
    WALKING('\uD83C','\uDFC3'),
    MAP('\uD83D','\uDDFA'),
    EXCLWARNING(null,'\u26A0'),
    HAPPY('\uD83E', '\uDD17'),
    CRYING_FACE('\uD83D', '\uDE22'),
    WHITE_HEAVY_CHECK_MARK(null, '\u2705'),
    HEAVY_PLUS_SIGN(null, '\u2795'),
    ALARM_CLOCK(null, '\u23F0'),
    BLACK_RIGHT_POINTING_TRIANGLE(null, '\u25B6'),
    WRENCH('\uD83D', '\uDD27'),
    ROUND_PUSHPIN('\uD83D', '\uDCCD'),
    WAVING_HAND_SIGN('\uD83D', '\uDC4B');

    Character firstChar;
    Character secondChar;

    Emoji(Character firstChar, Character secondChar) {
        this.firstChar = firstChar;
        this.secondChar = secondChar;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (this.firstChar != null) {
            sb.append(this.firstChar);
        }
        if (this.secondChar != null) {
            sb.append(this.secondChar);
        }

        return sb.toString();
    }
}
