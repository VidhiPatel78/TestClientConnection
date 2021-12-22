package com.text.with.sticker.textonphoto.quotesedit;

public class ComponentInfo {
    private int COMP_ID;
    private int HEIGHT;
    private int HUE;
    private int LEFT;
    private int OPACITY;
    private int ORDER;
    private float POS_X;
    private float POS_Y;
    private String RES_ID;
    private float ROTATION;
    private int TEMPLATE_ID;
    private int TOP;
    private int TOP_ID;
    private String TYPE = "";
    private int WIDTH;
    private float Y_ROTATION;

    public int getTOP() {
        return this.TOP;
    }

    public void setTOP(int TOP2) {
        this.TOP = TOP2;
    }

    public int getLEFT() {
        return this.LEFT;
    }

    public void setLEFT(int LEFT2) {
        this.LEFT = LEFT2;
    }

    public ComponentInfo() {
    }

    public ComponentInfo(int TEMPLATE_ID2, float POS_X2, float POS_Y2, int WIDTH2, int HEIGHT2, float ROTATION2, float Y_ROTATION2, String RES_ID2, String TYPE2, int ORDER2, int TOP_ID2, int TOP2, int LEFT2) {
        this.TEMPLATE_ID = TEMPLATE_ID2;
        this.POS_X = POS_X2;
        this.POS_Y = POS_Y2;
        this.WIDTH = WIDTH2;
        this.HEIGHT = HEIGHT2;
        this.ROTATION = ROTATION2;
        this.Y_ROTATION = Y_ROTATION2;
        this.RES_ID = RES_ID2;
        this.TYPE = TYPE2;
        this.ORDER = ORDER2;
        this.TOP_ID = TOP_ID2;
        this.TOP = TOP2;
        this.LEFT = LEFT2;
    }

    public int getCOMP_ID() {
        return this.COMP_ID;
    }

    public void setCOMP_ID(int COMP_ID2) {
        this.COMP_ID = COMP_ID2;
    }

    public int getTEMPLATE_ID() {
        return this.TEMPLATE_ID;
    }

    public void setTEMPLATE_ID(int TEMPLATE_ID2) {
        this.TEMPLATE_ID = TEMPLATE_ID2;
    }

    public float getPOS_X() {
        return this.POS_X;
    }

    public void setPOS_X(float POS_X2) {
        this.POS_X = POS_X2;
    }

    public float getPOS_Y() {
        return this.POS_Y;
    }

    public void setPOS_Y(float POS_Y2) {
        this.POS_Y = POS_Y2;
    }

    public String getRES_ID() {
        return this.RES_ID;
    }

    public void setRES_ID(String RES_ID2) {
        this.RES_ID = RES_ID2;
    }

    public String getTYPE() {
        return this.TYPE;
    }

    public void setTYPE(String TYPE2) {
        this.TYPE = TYPE2;
    }

    public int getORDER() {
        return this.ORDER;
    }

    public void setORDER(int ORDER2) {
        this.ORDER = ORDER2;
    }

    public int getTOP_ID() {
        return this.TOP_ID;
    }

    public void setTOP_ID(int TOP_ID2) {
        this.TOP_ID = TOP_ID2;
    }

    public float getROTATION() {
        return this.ROTATION;
    }

    public void setROTATION(float ROTATION2) {
        this.ROTATION = ROTATION2;
    }

    public int getWIDTH() {
        return this.WIDTH;
    }

    public void setWIDTH(int WIDTH2) {
        this.WIDTH = WIDTH2;
    }

    public int getHEIGHT() {
        return this.HEIGHT;
    }

    public void setHEIGHT(int HEIGHT2) {
        this.HEIGHT = HEIGHT2;
    }

    public float getY_ROTATION() {
        return this.Y_ROTATION;
    }

    public void setY_ROTATION(float y_ROTATION) {
        this.Y_ROTATION = y_ROTATION;
    }

    public int getHUE() {
        return this.HUE;
    }

    public void setHUE(int HUE2) {
        this.HUE = HUE2;
    }

    public int getOPACITY() {
        return this.OPACITY;
    }

    public void setOPACITY(int OPACITY2) {
        this.OPACITY = OPACITY2;
    }
}
