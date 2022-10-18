public class App {

    public static void main(String[] args) {
        Utils utils = new Utils();
        GITGITHUB integrate = new GITGITHUB();
        if (args.length == 0) {
            utils.ShowHelp();
            return;
        }
        switch (args[0]) {
            case "create":
                String name = utils.FindName(args);
                String ssh_utl = integrate.CreateRemoteRepo(name, utils.FindDescription(args), utils.FindToken(args));
                try {
                    integrate.CreateLocalRepo(ssh_utl, name);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case "destroy":
                String username = utils.FindUser(args);
                String reponame = utils.FindRepo(args);
                boolean isdestroyed = integrate.DeleteRemoteRepository(username, reponame, utils.FindToken(args));
                if (isdestroyed) {
                    System.out.println("Repository: '" + reponame + "' has been successfully deleted");
                }
                break;
            case "--help":
                utils.ShowHelp();
                break;
            default:
                System.out.println("Invalid Argument: " + args[0]);
                System.out.println("Use '--help' to find more.");
                break;
        }
    }
}
