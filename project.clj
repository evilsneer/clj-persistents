(defproject emptyone/persistents "0.1.3-SNAPSHOT"
  :description "Clojure persistents utils for working with files."
  :url "https://github.com/evilsneer/clj-persistents"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :plugins [[org.apache.maven.wagon/wagon-ssh-external "2.6"]]
  :deploy-repositories ^:replace [["releases" {:url           "scp://root@10.20.30.111/root/rel/"
                                               :sign-releases false}]
                                  ["snapshots" "scp://root@10.20.30.111/root/snap/"]]
  :dependencies [[org.clojure/clojure "1.10.0"]]
  :repl-options {:init-ns persistents.core})


(cemerick.pomegranate.aether/register-wagon-factory!
  "scp" #(clojure.lang.Reflector/invokeConstructor
           (resolve 'org.apache.maven.wagon.providers.ssh.external.ScpExternalWagon)
           (into-array [])))