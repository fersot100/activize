module.exports = function(sequelize, DataTypes) {
	module.exports = function (sequelize, DataTypes) {
	return sequelize.define('event', {
		name: {
			type: DataTypes.STRING,
			allowNull: false,
			validate: {
				len: [1, 50]
			}
		},
		description: {
			type: DataTypes.STRING,
			allowNull: false,
			validate: {
				len:[1, 250]
			}
		},
		startTime: {
			type: DataTypes.STRING
		},
		latlng: {
			type: DataTypes.STRING
		}
	});
}
}