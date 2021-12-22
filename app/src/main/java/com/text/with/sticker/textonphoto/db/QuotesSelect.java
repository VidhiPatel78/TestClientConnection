package com.text.with.sticker.textonphoto.db;

public class QuotesSelect {
    int _collum_id;
    String _color;
    String _end;
    String _font;
    int _id;
    String _shader;
    String _shadow_color;
    String _shadow_dx;
    String _shadow_dy;
    String _shadow_radius;
    String _size;
    String _start;
    int _text_id;
    String _text_italic;
    String _text_strik;
    String _text_underline;
    String _textbold;

    public QuotesSelect() {
    }

    public QuotesSelect(int collum_id, int text_id, String start, String end, String size, String color, String font, String shadow_dx, String dhadow_dy, String shadow_radius, String shadow_color, String shader, String textbold, String text_italic, String text_underline, String text_strik) {
        this._collum_id = collum_id;
        this._text_id = text_id;
        this._start = start;
        this._end = end;
        this._size = size;
        this._color = color;
        this._font = font;
        this._shadow_dx = shadow_dx;
        this._shadow_dy = dhadow_dy;
        this._shadow_radius = shadow_radius;
        this._shadow_color = shadow_color;
        this._shader = shader;
        this._textbold = textbold;
        this._text_italic = text_italic;
        this._text_underline = text_underline;
        this._text_strik = text_strik;
    }

    public int getID() {
        return this._id;
    }

    public void setID(int id) {
        this._id = id;
    }

    public int get_id() {
        return this._id;
    }

    public void set_id(int _id2) {
        this._id = _id2;
    }

    public int get_collum_id() {
        return this._collum_id;
    }

    public void set_collum_id(int _collum_id2) {
        this._collum_id = _collum_id2;
    }

    public int get_text_id() {
        return this._text_id;
    }

    public void set_text_id(int _text_id2) {
        this._text_id = _text_id2;
    }

    public String get_start() {
        return this._start;
    }

    public void set_start(String _start2) {
        this._start = _start2;
    }

    public String get_end() {
        return this._end;
    }

    public void set_end(String _end2) {
        this._end = _end2;
    }

    public String get_size() {
        return this._size;
    }

    public void set_size(String _size2) {
        this._size = _size2;
    }

    public String get_color() {
        return this._color;
    }

    public void set_color(String _color2) {
        this._color = _color2;
    }

    public String get_font() {
        return this._font;
    }

    public void set_font(String _font2) {
        this._font = _font2;
    }

    public String get_shadow_dx() {
        return this._shadow_dx;
    }

    public void set_shadow_dx(String _shadow_dx2) {
        this._shadow_dx = _shadow_dx2;
    }

    public String get_shadow_dy() {
        return this._shadow_dy;
    }

    public void set_shadow_dy(String _sdhadow_dy) {
        this._shadow_dy = _sdhadow_dy;
    }

    public String get_shadow_radius() {
        return this._shadow_radius;
    }

    public void set_shadow_radius(String _shadow_radius2) {
        this._shadow_radius = _shadow_radius2;
    }

    public String get_shadow_color() {
        return this._shadow_color;
    }

    public void set_shadow_color(String _shadow_color2) {
        this._shadow_color = _shadow_color2;
    }

    public String get_shader() {
        return this._shader;
    }

    public void set_shader(String _shader2) {
        this._shader = _shader2;
    }

    public String get_textbold() {
        return this._textbold;
    }

    public void set_textbold(String _textbold2) {
        this._textbold = _textbold2;
    }

    public String get_text_italic() {
        return this._text_italic;
    }

    public void set_text_italic(String _text_italic2) {
        this._text_italic = _text_italic2;
    }

    public String get_text_underline() {
        return this._text_underline;
    }

    public void set_text_underline(String _text_underline2) {
        this._text_underline = _text_underline2;
    }

    public String get_text_strik() {
        return this._text_strik;
    }

    public void set_text_strik(String _text_strik2) {
        this._text_strik = _text_strik2;
    }
}
