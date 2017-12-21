

from bs4 import BeautifulSoup
import requests
import json




class Hour(object):
	def __init__(self, hour, minute):
		self.hour=int(hour[0:2])
		try: 
			int(minute[0:2])
			self.minute=int(minute[0:2])
			self.time=hour+":"+minute[0:2]
	
		except ValueError:
			self.minute=0
			self.time=hour+":00"

	def serialize(self):
		return {'hour': self.hour, 'minute': self.minute,'time': self.time,}




class Route(object):


	def __init__(self, idr, route_official, route_name):
		self.route_name=route_name.split("-")[0]
		self.route_official=route_official
		self.idr=idr
		self.points=[]
		self.hours=[]
		if(self.route_name==" "):
			self.route_name="Desconhecido"
		else:
			self.route_name=self.route_name[0:len(self.route_name)-1]

	def __str__(self):
		return "ID: "+ str(self.idr)+"    Route_Name: "+ self.route_name+"    Route Official: "+self.route_official+"    Points: "+ self.printPoints()+ "    Hours: "+self.printHours()

	def printHours(self):
		result=""
		for i in self.hours:
			result=result+i.time+" - "
		result=result[0:int(len(result)-3)]
		return result

	def printPoints(self):
		result=""
		for i in self.points:
			result=result+i+" - "
		result=result[0:int(len(result)-3)]
		return result

	def serialize(self):
		return {'id': self.idr,'route_name': self.route_name, 'points': [ob for ob in self.points],}

	def serialize_all(self):
		return {'id': self.idr,'route_name': self.route_name, 'route_official': self.route_official,'points': [ob for ob in self.points],'hours': [obx.serialize() for obx in self.hours],}



def getRoutes(debug):

	id_counter=0

	r  = requests.get("http://smtuc.pt/top_horarios.php?titulo=Horarios&tipo_linha=0")

	data = r.text

	souphome = BeautifulSoup(data, 'html.parser')


	routes=[]

	counter =1

	for i in souphome.find('select').find_all('option'):
		if(counter==1):
			counter=counter-1
			continue

		r  = requests.get("http://smtuc.pt/horario.php?id_linha="+i['value'])

		data = r.text

		soup = BeautifulSoup(data, 'html.parser')
		

		stuff=[]
		for uls in soup.find_all('td',width="878") :
			stuff.append(uls.text.strip('\n'))





		route=Route(id_counter,i['value'], i.text)

		id_counter=id_counter+1
		route.points.append(stuff[0].strip('\n'))
		route.points.append(stuff[1].strip('\n'))
		


		cont=0

		listchildren = [x.text for x in soup.find_all('th',bgcolor="#FF9900")]




		
		listfirst=listchildren[1:(int(len(listchildren)/2))]

		listsecond=listchildren[(int(len(listchildren)/2))+1:len(listchildren)]





		listtables = [x.text for x in soup.find_all('td',width=39)]



		if(len(listfirst)+len(listsecond)==0):
			continue
		height=int((len(listtables))/(len(listfirst)))





		matrix = [[listtables[y*len(listfirst)+x] for x in range(len(listfirst))] for y in range(height)]


		if(height==20):



			for k in range (0,int(height/2)):
				for f in range(0,len(listfirst)):

					try: 
						int(matrix[k][f][0:2])
						route.hours.append(Hour(listfirst[f],matrix[k][f]))
			
					except ValueError:
						continue

			route.hours=sorted(route.hours, key=lambda x: (x.hour, x.minute), reverse=False)

			routes.append(route)

			if(debug):
				print(routes[len(routes)-1])
				print("\n")




			route=Route(id_counter,i['value'], i.text)

			id_counter=id_counter+1

			route.points.append(stuff[1].strip('\n'))
			route.points.append(stuff[0].strip('\n'))


			for k in range (int(height/2),int(height)):
				for f in range(0,len(listfirst)):

					try: 
						int(matrix[k][f][0:2])
						route.hours.append(Hour(listfirst[f],matrix[k][f]))
			
					except ValueError:
						continue

			route.hours=sorted(route.hours, key=lambda x: (x.hour, x.minute), reverse=False)

			routes.append(route)

			if(debug):
				print(routes[len(routes)-1])
				print("\n")

		else:


			for k in range (0,int(height/4)):
				for f in range(0,len(listfirst)):

					try: 
						int(matrix[k][f][0:2])
						route.hours.append(Hour(listfirst[f],matrix[k][f]))
			
					except ValueError:
						continue

			route.hours=sorted(route.hours, key=lambda x: (x.hour, x.minute), reverse=False)

			routes.append(route)

			if(debug):
				print(routes[len(routes)-1])
				print("\n")




			route=Route(id_counter,i['value'], i.text)

			id_counter=id_counter+1
			route.points.append(stuff[1].strip('\n'))
			route.points.append(stuff[0].strip('\n'))


			for k in range (int(height/2),int(height/4*3)):
				for f in range(0,len(listfirst)):

					try: 
						int(matrix[k][f][0:2])
						route.hours.append(Hour(listfirst[f],matrix[k][f]))
			
					except ValueError:
						continue

			route.hours=sorted(route.hours, key=lambda x: (x.hour, x.minute), reverse=False)

			routes.append(route)


			if(debug):
				print(routes[len(routes)-1])
				print("\n")

	return routes


if __name__=='__main__':
	getRoutes(True)