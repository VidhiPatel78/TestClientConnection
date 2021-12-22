package com.text.with.sticker.textonphoto.quotesedit;

public class SelectedTextData {
    int end;
    int start;
    String text;
    boolean text_bold = false;
    int text_color;
    boolean text_italic = false;
    String text_shader;
    int text_shadowcolor;
    float text_shadowdx = 0.0f;
    float text_shadowdy = 0.0f;
    float text_shadowradius = 0.0f;
    int text_size = 0;
    boolean text_strike = false;
    String text_ttf;
    boolean text_underline = false;

    public int getStart() {
        return this.start;
    }

    public void setStart(int start2) {
        this.start = start2;
    }

    public int getEnd() {
        return this.end;
    }

    public void setEnd(int end2) {
        this.end = end2;
    }

    public int getText_size() {
        return this.text_size;
    }

    public void setText_size(int text_size2) {
        this.text_size = text_size2;
    }

    public String getText_ttf() {
        return this.text_ttf;
    }

    public void setText_ttf(String text_ttf2) {
        this.text_ttf = text_ttf2;
    }

    public int getText_color() {
        return this.text_color;
    }

    public void setText_color(int text_color2) {
        this.text_color = text_color2;
    }

    public float getText_shadowdx() {
        return this.text_shadowdx;
    }

    public void setText_shadowdx(float text_shadowdx2) {
        this.text_shadowdx = text_shadowdx2;
    }

    public float getText_shadowdy() {
        return this.text_shadowdy;
    }

    public void setText_shadowdy(float text_shadowdy2) {
        this.text_shadowdy = text_shadowdy2;
    }

    public float getText_shadowradius() {
        return this.text_shadowradius;
    }

    public void setText_shadowradius(float text_shadowradius2) {
        this.text_shadowradius = text_shadowradius2;
    }

    public int getText_shadowcolor() {
        return this.text_shadowcolor;
    }

    public void setText_shadowcolor(int text_shadowcolor2) {
        this.text_shadowcolor = text_shadowcolor2;
    }

    public String getText_shader() {
        return this.text_shader;
    }

    public void setText_shader(String text_shader2) {
        this.text_shader = text_shader2;
    }

    public boolean isText_bold() {
        return this.text_bold;
    }

    public void setText_bold(boolean text_bold2) {
        this.text_bold = text_bold2;
    }

    public boolean isText_italic() {
        return this.text_italic;
    }

    public void setText_italic(boolean text_italic2) {
        this.text_italic = text_italic2;
    }

    public boolean isText_underline() {
        return this.text_underline;
    }

    public void setText_underline(boolean text_underline2) {
        this.text_underline = text_underline2;
    }

    public boolean isText_strike() {
        return this.text_strike;
    }

    public void setText_strike(boolean text_strike2) {
        this.text_strike = text_strike2;
    }
}
