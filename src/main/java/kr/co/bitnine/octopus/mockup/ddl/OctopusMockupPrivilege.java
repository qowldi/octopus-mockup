package kr.co.bitnine.octopus.mockup.ddl;

class OctopusMockupPrivilege extends OctopusMockupCommand
{
    final String objectName;
    final String user;
    private final Type type;

    OctopusMockupPrivilege(String objectName, String user, Type type)
    {
        this.objectName = objectName;
        this.user = user;
        this.type = type;
    }

    @Override
    public Type getType()
    {
        return type;
    }
}
