
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Version;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GITGITHUB {

    private ContainerFactory containerFactory = new ContainerFactory() {
        @Override
        public List<Object> creatArrayContainer() {
            return new LinkedList<Object>();
        }

        @Override
        public Map<String, Object> createObjectContainer() {
            return new LinkedHashMap<String, Object>();
        }

    };

    public String CreateRemoteRepo(String name, String desc, String token) {
        Utils utils = new Utils();
        utils.ShowLogo();
        System.out.println("Creating Remote Repository on Github...");
        HashMap<String, Object> body = new HashMap<String, Object>();
        body.put("name", name);
        body.put("description", desc);
        body.put("homepage", "https://github.com");
        body.put("private", false);
        body.put("is_template", false);

        JSONObject obj = new JSONObject();
        obj.putAll(body);

        String str = obj.toJSONString();

        String uri = "https://api.github.com/user/repos";
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Accept", "application/vnd.github+json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(str))
                .version(Version.HTTP_2)
                .build();

        HttpClient client = HttpClient.newBuilder()
                .build();
        String ssh_url = "";
        try {
            HttpResponse<String> response = client.send(req, BodyHandlers.ofString());
            System.out.println(response.statusCode());
            Map<?, ?> bodyObj;
            try {
                bodyObj = (Map<?, ?>) new JSONParser().parse(response.body(), containerFactory);
                ssh_url = (String) bodyObj.get("ssh_url");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ssh_url;
    }

    public boolean CreateLocalRepo(String ssh_url, String name) throws InterruptedException {
        System.out.println("Creating Local Repository...");
        String[] c1 = { "git", "init" };
        String[] c2 = { "git", "add", "readme.md" };
        String[] c3 = { "git", "commit", "-m", "'first commit'" };
        String[] c4 = { "git", "branch", "-M", "main" };
        String[] c5 = { "git", "remote", "add", "origin", ssh_url };
        String[] c6 = { "git", "push", "-u", "origin", "main" };

        boolean isCreated = CreateReadmeFile(name);
        if (isCreated) {
            if (Run(c1)) {
                if (Run(c2)) {
                    if (Run(c3)) {
                        if (Run(c4)) {
                            if (Run(c5)) {
                                if (Run(c6)) {
                                    System.out.println("Local repository has been successfully created");
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("[ERROR] -> Something went wrong while setting up local repo...");
        return false;
    }

    private boolean Run(String[] commands) throws InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.directory(new File(System.getProperty("user.dir")));
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[SUCCESS] -> " + line);
            }

            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                System.out.println("[INFO] -> " + errorLine);
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.out.println("[ERROR_CODE] -> " + String.valueOf(exitCode));
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean CreateReadmeFile(String name) {
        try {
            FileWriter writer = new FileWriter(System.getProperty("user.dir") + "/readme.md", true);
            writer.write("# " + name);
            writer.write("\r\n"); // write new line
            writer.write("### This file generated by GGI");
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean DeleteRemoteRepository(String userName, String repoName, String token) {
        Utils utils = new Utils();
        utils.ShowLogo();
        System.out.println("Deleting Remote Github repository: " + repoName);
        String uri = "https://api.github.com/repos/" + userName + "/" + repoName;
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Accept", "application/vnd.github+json")
                .header("Authorization", "Bearer " + token)
                .DELETE()
                .version(Version.HTTP_2)
                .build();

        HttpClient client = HttpClient.newBuilder()
                .build();
        try {
            HttpResponse<String> response = client.send(req, BodyHandlers.ofString());
            if (response.statusCode() == 204) {
                return true;
            }
            return false;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
