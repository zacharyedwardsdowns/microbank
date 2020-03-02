# A global argument containing the working directory for the service.
ARG workingDirectory=/microbank/angular

# The maven build stage of the service.
FROM node:alpine as build
ARG workingDirectory
WORKDIR ${workingDirectory}
COPY package.json package-lock.json ${workingDirectory}/
RUN npm set progress=false && npm install
COPY . .
RUN npm run build

# Run the registration service.
FROM nginx:alpine
ARG nginxDirectory=/usr/share/nginx/html
ARG workingDirectory
WORKDIR ${nginxDirectory}
RUN rm -rf *
COPY --from=build ${workingDirectory}/dist .
COPY --from=build ${workingDirectory}/nginx.conf /etc/nginx/conf.d/default.conf
CMD ["nginx", "-g", "daemon off;"]
