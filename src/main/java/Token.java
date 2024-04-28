// This enum contains all of the tokens the language consists of.
public enum Token
{
    TOKENSEMI (";"),
    KEYWORDPRINT ("print"),
    KEYWORDGET ("get"),
    OPERATORLEQ ("<="),
    OPERATORGEQ (">="),
    OPERATOREQ ("=="),
    OPERATORNEQ ("!="),
    OPERATORASSIGN ("="),
    KEYWORDIF ("if"),
    KEYWORDTHEN ("then"),
    KEYWORDELSE ("else"),
    KEYWORDEND ("end"),
    KEYWORDWHILE ("while"),
    KEYWORDFOR ("for"),
    KEYWORDEACH ("each"),
    KEYWORDIN ("in"),
    KEYWORDDO ("do"),
    KEYWORDAND ("and"),
    KEYWORDOR ("or"),
    OPERATORADD ("[+]"),
    OPERATORMIN ("[-]"),
    OPERATORMUL ("[*]"),
    OPERATORDIV ("[/]"),
    OPERATORMOD ("[%]"),
    OPERATORGT ("[>]"),
    OPERATORLT ("[<]"),
    TOKENCOMMA ("[,]"),
    TOKENLPAR ("[(]"),
    TOKENLBRACK ("[\\[]"),
    TOKENRBRACK ("[\\]]"),
    TOKENLBRACE ("[\\{]"),
    TOKENRBRACE ("[\\}]"),
    TOKENRPAR ("[)]"),
    KEYWORDNOT ("not"),
    
    TOKENSTRING("\"([^\"]|\\\")*\""),
    TOKENID ("[_a-zA-Z][_a-zA-Z0-9]*"),
    TOKENINT ("(-)?[0-9]+");
    
    public final String pattern;    // This String contains a regex pattern.
    
    // This constructor initializes pattern with the String r.
    Token(String r)
    {
        pattern = r;
    }
}
