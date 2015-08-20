grammar OctopusMockupDdl;

@header {
    import org.apache.commons.lang3.StringUtils;
}

ddl
    : ( ddlStmts | error ) EOF
    ;

ddlStmts
    : ';'* ddlStmt ';'*
    ;

ddlStmt
    : createUser
    | grantSelect
    | revokeSelect
    ;

createUser
    : K_CREATE K_USER user K_IDENTIFIED K_BY password
    ;

user
    : IDENTIFIER
    ;

password
    : STRING_LITERAL
    ;

grantSelect
    : K_GRANT K_SELECT K_ON objectName K_TO user
    ;

revokeSelect
    : K_REVOKE K_SELECT K_ON objectName K_FROM user
    ;

objectName
    : IDENTIFIER
    ;

error
    : UNEXPECTED_CHAR
        {
            throw new RuntimeException("UNEXPECTED_CHAR=" + $UNEXPECTED_CHAR.text);
        }
    ;

K_BY : B Y ;
K_CREATE : C R E A T E ;
K_FROM : F R O M ;
K_GRANT : G R A N T ;
K_IDENTIFIED : I D E N T I F I E D ;
K_ON : O N ;
K_REVOKE : R E V O K E ;
K_SELECT : S E L E C T ;
K_TO : T O ;
K_USER : U S E R ;

IDENTIFIER
    : '"' ( ~["\r\n] | '""' )* '"'
        {
            setText(StringUtils.strip(getText(), "\"").replace("\"\"", "\""));
        }
    | '`' ( ~[`\r\n] | '``' )* '`'
        {
            setText(StringUtils.strip(getText(), "`").replace("``", "`"));
        }
    | '[' ~[\]\r\n]* ']'
    | LETTER ( LETTER | DIGIT )*
    ;

STRING_LITERAL
    : '\'' ( ~['\r\n] | '\'\'' )* '\''
        {
            setText(StringUtils.strip(getText(), "'").replace("''", "'"));
        }
    ;

WHITESPACES : [ \t\r\n]+ -> channel(HIDDEN) ;

UNEXPECTED_CHAR : . ;

fragment A : [aA] ;
fragment B : [bB] ;
fragment C : [cC] ;
fragment D : [dD] ;
fragment E : [eE] ;
fragment F : [fF] ;
fragment G : [gG] ;
fragment H : [hH] ;
fragment I : [iI] ;
fragment J : [jJ] ;
fragment K : [kK] ;
fragment L : [lL] ;
fragment M : [mM] ;
fragment N : [nN] ;
fragment O : [oO] ;
fragment P : [pP] ;
fragment Q : [qQ] ;
fragment R : [rR] ;
fragment S : [sS] ;
fragment T : [tT] ;
fragment U : [uU] ;
fragment V : [vV] ;
fragment W : [wW] ;
fragment X : [xX] ;
fragment Y : [yY] ;
fragment Z : [zZ] ;

fragment LETTER : [a-zA-Z_] ;
fragment DIGIT : [0-9] ;
