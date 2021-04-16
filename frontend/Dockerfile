FROM node:10

# Create app directory
WORKDIR /app
ADD . /app/

# global install & update
RUN npm install
RUN npm run build

EXPOSE 3000

# start command
CMD [ "npm", "run", "start" ]
