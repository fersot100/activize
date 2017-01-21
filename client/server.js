var express = require('express');
var app = express();
var _ = require('underscore');
var bodyParser = require('body-parser');
var PORT = 3000;
var db = require('./db.js');

app.use(bodyParser.json());

//POST Events
app.post('/events', function(req, res){
	var body = req.body
	console.log(body);
	db.events.create(body).then(function(event){
		res.json(event.toJSON());
	}, function (e) {
		res.status(400).json(e);
	});
});

// GET Events
app.get('/events', function(req, res){
	db.events.findAll().then(function(events) {
		if(typeof events == 'null'){
			res.status(404).send('No events exist');
		}
		console.log('Events Retrieved');
		res.json(events);
	}, function (e) {
		res.status(500).send();
	});
});