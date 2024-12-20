1) AWS setup m5.large Cloud9 EC2 instance

2) Install SDKMAN   (https://sdkman.io/)
  curl -s "https://get.sdkman.io" | bash

  source "/home/ec2-user/.sdkman/bin/sdkman-init.sh"

3) Install GraalVM 21  (https://www.graalvm.org/latest/docs/getting-started/linux/)
   sdk install java 23.0.1-graal

4) Install Native Image  (https://www.graalvm.org/latest/reference-manual/native-image/)

   sudo yum install gcc glibc-devel zlib-devel
  
   sudo dnf install gcc glibc-devel zlib-devel libstdc++-static

5) Install Maven  

   yum install maven 

6) Clone git Repo
  git clone https://github.com/Vadym79/AWSLambdaJavaWithSpringBoot.git

7) Build Native Image

   mvn clean package -P native

8)Connect Cloud9 to GitHub (https://aws.plainenglish.io/how-to-use-aws-cloud9-with-github-3136692fa44d)

9) Resize Cloud9 Storage if required https://ec2spotworkshops.com/ecs-spot-capacity-providers/workshopsetup/resize_ebs.html