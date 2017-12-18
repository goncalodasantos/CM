from flask import Flask, jsonify
from flask import jsonify
import Crawler.crawler as cr
import pickle




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


debug=False



app = Flask(__name__)



@app.route('/',methods=['GET'])
def get_routes():



	resp = {}

	resp['data'] = routes[0].serialize()

	print(routes[0].serialize())

	return jsonify(resp), 200












app.run(debug=debug)
