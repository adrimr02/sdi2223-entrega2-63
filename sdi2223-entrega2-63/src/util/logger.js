const {createLogger, format, transports} = require("winston");
let Winston = require('winston-mongodb');

Winston = createLogger({
    transports: [
        new transports.MongoDB({
            db: 'mongodb://127.0.0.1:27017/mywallapop',
            level: 'info',
            collection: 'logs',
            format: format.combine(format.timestamp(), format.json()),
            options: { useUnifiedTopology: true }
        })
    ]
});

module.exports = Winston;