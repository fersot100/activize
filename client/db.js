var Sequelize = require('sequelize');
var sequelize = new Sequelize(undefined, undefined, undefined, {
	'dialect': 'sqlite',
	'storage': __dirname + '/data/core-database.sqlite'
});s

var db = {};

db.events = sequelize.import(__dirname + '/models/event.js');
db.sequelize = sequelize;
db.Sequelize = Sequelize;

module.exports = db;