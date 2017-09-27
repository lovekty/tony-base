namespace java me.tony.base.thrift.gen

service DemoService {
string getName(1:string prefix)
i32 getAge()
}