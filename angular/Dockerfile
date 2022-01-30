# A global argument containing the working directory for the frontend.
ARG workingDirectory=/usr/share/nginx/html

# Run the angular frontend.
FROM nginx:alpine
ARG workingDirectory
WORKDIR ${workingDirectory}
RUN rm -rf *
COPY ./dist .
COPY ./https ./https
COPY ./nginx.conf /etc/nginx/conf.d/default.conf
CMD ["nginx", "-g", "daemon off;"]
