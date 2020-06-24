package me.Sam.ItemFilter;

public enum Msg {
    DIDYOUKNOW(Utils.prefix + ItemFilter.instance.messages.getString("DidYouKnow")),
    OPENINGFILTER (Utils.prefix + ItemFilter.instance.messages.getString("OpeningFilter"));

    public String msg;

    private Msg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }
}
