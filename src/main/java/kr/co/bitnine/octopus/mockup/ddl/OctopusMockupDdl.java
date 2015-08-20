package kr.co.bitnine.octopus.mockup.ddl;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.sql.SQLException;

final public class OctopusMockupDdl
{
    private OctopusMockupDdl() { }

    private static class Listener extends OctopusMockupDdlBaseListener
    {
        private OctopusMockupCommand cmd = null;

        @Override
        public void exitCreateUser(OctopusMockupDdlParser.CreateUserContext ctx)
        {
            String name = ctx.user().getText();
            String password = ctx.password().getText();
            cmd = new OctopusMockupCreateUser(name, password);
        }

        @Override
        public void exitGrantSelect(OctopusMockupDdlParser.GrantSelectContext ctx)
        {
            String objectName = ctx.objectName().getText();
            String grantee = ctx.user().getText();
            cmd = new OctopusMockupPrivilege(objectName, grantee, OctopusMockupCommand.Type.GRANT_SELECT);
        }

        @Override
        public void exitRevokeSelect(OctopusMockupDdlParser.RevokeSelectContext ctx)
        {
            String objectName = ctx.objectName().getText();
            String revokee = ctx.user().getText();
            cmd = new OctopusMockupPrivilege(objectName, revokee, OctopusMockupCommand.Type.REVOKE_SELECT);
        }

        OctopusMockupCommand getCommand()
        {
            return cmd;
        }
    }

    public static OctopusMockupCommand parse(String sqlString)
    {
        ANTLRInputStream input = new ANTLRInputStream(sqlString);
        OctopusMockupDdlLexer lexer = new OctopusMockupDdlLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        OctopusMockupDdlParser parser = new OctopusMockupDdlParser(tokens);
        ParserRuleContext tree = parser.ddl();

        ParseTreeWalker walker = new ParseTreeWalker();
        Listener lsnr = new Listener();
        walker.walk(lsnr, tree);

        return lsnr.getCommand();
    }

    public static void run(OctopusMockupCommand cmd, OctopusMockupRunner runner) throws SQLException
    {
        switch (cmd.getType()) {
            case CREATE_USER:
                OctopusMockupCreateUser createUser = (OctopusMockupCreateUser) cmd;
                runner.createUser(createUser.name, createUser.password);
                break;
            case GRANT_SELECT:
                OctopusMockupPrivilege grantSelect = (OctopusMockupPrivilege) cmd;
                runner.grantSelect(grantSelect.objectName, grantSelect.user);
                break;
            case REVOKE_SELECT:
                OctopusMockupPrivilege revokeSelect = (OctopusMockupPrivilege) cmd;
                runner.revokeSelect(revokeSelect.objectName, revokeSelect.user);
                break;
            default:
                throw new RuntimeException("invalid DDL command");
        }
    }
}
