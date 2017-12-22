from flask import Flask, jsonify
from flask import jsonify
import Crawler.crawler as cr
import pickle


debug=False


try:
	with open('data.txt', 'rb') as data_file:
		routes = pickle.load(data_file)
	print("loaded info from file")

except FileNotFoundError:
	print("running, fetching data")
	routes=cr.getRoutes(debug)
	print("data fetched, bro")
	with open('data.txt', 'wb') as outfile:
		pickle.dump(routes, outfile)





app = Flask(__name__)



@app.route('/route/',methods=['GET'])
def get_routes():



	resp = {}

	resp['data'] = []


	for c in routes:

		resp['data'].append(c.serialize())


	return jsonify(resp), 200





@app.route('/route/information/',methods=['GET'])
def get_routes_information():




	resp = {}

	resp['data'] = []


	for c in routes:

		resp['data'].append(c.serialize_all())

	return jsonify(resp), 200





@app.route('/route/<int:ids>',methods=['GET'])
def get_route(ids):



	resp = {}

	for c in routes:
		if(c.idr==ids):
			resp['data'] = c.serialize()
			return jsonify(resp), 200






	return jsonify(resp), 400












app.run(debug=debug, host='0.0.0.0')
