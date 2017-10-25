from motor.models import FeedingTime
class Data():
	temperature=0
	illuminance = 0
	turbidity = 0
	time = FeedingTime.objects.get(pk=1).lasttime
