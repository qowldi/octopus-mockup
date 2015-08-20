package kr.co.bitnine.octopus.mockup.ddl;

class OctopusMockupCreateUser extends OctopusMockupCommand
{
    final String name;
    final String password;

    OctopusMockupCreateUser(String name, String password)
    {
        this.name = name;
        this.password = password;
    }

    @Override
    public Type getType()
    {
        return Type.CREATE_USER;
    }
}
