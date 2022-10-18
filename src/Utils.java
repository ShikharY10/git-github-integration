public class Utils {
    public String FindName(String[] arr) {
        String name;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals("-n")) {
                name = arr[i + 1];
                return name;
            }
        }
        return "";
    }

    public String FindDescription(String[] arr) {
        String desc;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals("-d")) {
                desc = arr[i + 1];
                return desc;
            }
        }
        return "";
    }

    public String FindToken(String[] arr) {
        String token;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals("-t")) {
                token = arr[i + 1];
                return token;
            }
        }
        return "";
    }

    public String FindUser(String[] arr) {
        String user;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals("-u")) {
                user = arr[i + 1];
                return user;
            }
        }
        return "";
    }

    public String FindRepo(String[] arr) {
        String repo;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals("-r")) {
                repo = arr[i + 1];
                return repo;
            }
        }
        return "";
    }

    public void ShowLogo() {
        System.out.println("");
        System.out.println("  00000000   00000000   00000000");
        System.out.println("  00    00   00    00      00");
        System.out.println("  00         00            00");
        System.out.println("  00  0000   00  0000      00");
        System.out.println("  00    00   00    00      00     Version: 0.0.1");
        System.out.println("  00000000   00000000   00000000  GIT GITHUB INTEGRATION");
        System.out.println("");
    }

    public void ShowHelp() {
        ShowLogo();
        System.out.println("A tool for automatically integrating Git and Github");
        System.out.println("It requires a OAUTH token with spcific priveleges");
        System.out.println("Awailable Commands:");
        System.out.println("\tcreate  -> It first create a remote repo at github and then create a local git repo");
        System.out.println("\t\tIt also create readme.md file and also create the first commit");
        System.out.println("\tExample: java App create -n <NAME_OF_REPO> -d <DESCRIPTION> -t <OAUTH_TOKEN>");
        System.out.println("\tdestroy  -> It delete the remote github repository.");
        System.out.println("\tExample: java App destroy -u <GITHUB_USERNAME> -r <REPO_NAME> -t <OAUTH_TOKEN>");
    }
}
