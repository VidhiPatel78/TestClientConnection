package com.text.with.sticker.textonphoto.db;

public class Quotes {
    int _collum_id;
    String _color;
    String _font;
    int _gravity;
    int _id;
    String _logo_image;
    String _scale;
    String _shader;
    String _shadow_color;
    String _shadow_dx;
    String _shadow_dy;
    String _shadow_radius;
    String _size;
    String _template;
    String _text;
    String _text_italic;
    String _text_strik;
    String _text_underline;
    String _textbold;
    String _user_image;
    int blur;
    String filter;
    int filter_percentage;
    String hasAuthor;
    String isCropped;
    int order;
    int pos_x;
    int pos_y;
    int rotation;

    public int getBlur() {
        return this.blur;
    }

    public void setBlur(int blur2) {
        this.blur = blur2;
    }

    public String getFilter() {
        return this.filter;
    }

    public void setFilter(String filter2) {
        this.filter = filter2;
    }

    public Quotes() {
    }

    public Quotes(String template, int collum_id, int gravity, String scale, String text, String size, String color, String font, String shadow_dx, String dhadow_dy, String shadow_radius, String shadow_color, String shader, String textbold, String text_italic, String text_underline, String text_strik, int pos_X, int pos_Y, int rotation2, String _user_image2, String logo_image, String hasAuthor2, int blur2, String filter2, int filter_percentage2, String isCropped2, int textOrder) {
        this._template = template;
        this._collum_id = collum_id;
        this._gravity = gravity;
        this._scale = scale;
        this._text = text;
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
        this.pos_x = pos_X;
        this.pos_y = pos_Y;
        this.rotation = rotation2;
        this._user_image = _user_image2;
        this._logo_image = logo_image;
        this.hasAuthor = hasAuthor2;
        this.blur = blur2;
        this.filter = filter2;
        this.filter_percentage = filter_percentage2;
        this.isCropped = isCropped2;
        this.order = textOrder;
    }

    public String get_template() {
        return this._template;
    }

    public void set_template(String _template2) {
        this._template = _template2;
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

    public int get_gravity() {
        return this._gravity;
    }

    public void set_gravity(int _gravity2) {
        this._gravity = _gravity2;
    }

    public String get_scale() {
        return this._scale;
    }

    public void set_scale(String _scale2) {
        this._scale = _scale2;
    }

    public String get_text() {
        return this._text;
    }

    public void set_text(String _text2) {
        this._text = _text2;
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

    public int getPos_x() {
        return this.pos_x;
    }

    public void setPos_x(int pos_x2) {
        this.pos_x = pos_x2;
    }

    public int getPos_y() {
        return this.pos_y;
    }

    public void setPos_y(int pos_y2) {
        this.pos_y = pos_y2;
    }

    public int getRotation() {
        return this.rotation;
    }

    public void setRotation(int rotation2) {
        this.rotation = rotation2;
    }

    public String get_logo_image() {
        return this._logo_image;
    }

    public void set_logo_image(String _logo_image2) {
        this._logo_image = _logo_image2;
    }

    public String get_user_image() {
        return this._user_image;
    }

    public void set_user_image(String _user_image2) {
        this._user_image = _user_image2;
    }

    public String getHasAuthor() {
        return this.hasAuthor;
    }

    public void setHasAuthor(String hasAuthor2) {
        this.hasAuthor = hasAuthor2;
    }

    public int getFilter_percentage() {
        return this.filter_percentage;
    }

    public void setFilter_percentage(int filter_percentage2) {
        this.filter_percentage = filter_percentage2;
    }

    public String getIsCropped() {
        return this.isCropped;
    }

    public void setIsCropped(String isCropped2) {
        this.isCropped = isCropped2;
    }

    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order2) {
        this.order = order2;
    }
}
