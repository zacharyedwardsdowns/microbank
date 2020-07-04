# A global argument containing the working directory for the service.
ARG workingDirectory=/usr/share/nginx/html

# Run the angular frontend.
FROM nginx:alpine
ARG nginxDirectory=/usr/share/nginx/html
ARG workingDirectory
WORKDIR ${workingDirectory}
RUN rm -rf *
COPY ./dist .
COPY ./nginx.conf /etc/nginx/conf.d/default.conf
CMD ["nginx", "-g", "daemon off;"]
