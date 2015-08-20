package kr.co.bitnine.octopus.mockup.ddl;

public abstract class OctopusMockupCommand
{
    enum Type
    {
        CREATE_USER,
        GRANT_SELECT,
        REVOKE_SELECT,
        OTHER
    }

    public Type getType()
    {
        return Type.OTHER;
    }
}
