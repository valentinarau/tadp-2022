class Module
  @@blocks_before = []
  @@blocks_after = []

  def before(&block)
    @@blocks_before << block
  end

  def after(&block)
    @@blocks_after << block
  end

  def execute_before
    @@blocks_before.each { |b| b.call }
  end

  def execute_after
    @@blocks_after.each { |b| b.call }
  end
end

class Persona

  before { puts "BLOCK BEFORE" }
  after { puts "BLOCK AFTER" }

  @@wrapped_methods = []

  def self.method_added(name)
    if @@wrapped_methods.include?(name) # el metodo ya fue wrappeado y se está redefiniendo
      @@wrapped_methods.delete(name)
    else # el metodo se está definiendo por primera vez o redefiniendo después de wrappearlo
      @@wrapped_methods << name
      orig_meth = instance_method(name)
      define_method(name) do |*args, &block|
        singleton_class.execute_before
        orig_meth.bind(self).call *args, &block
        singleton_class.execute_after
      end
    end
  end

  def edad
    puts 'la edad es ' + @edad.to_s
    @edad
  end

  def set_edad(n)
    puts 'seteando edad en ' + n.to_s
    @edad = n
  end
end



##### Pruebas #####
p = Persona.new
p.edad
p.set_edad(10)
p.edad

# se abre la clase Persona
# se redefine 'edad' y vuelve a wrappearse
class Persona
  def edad
    100
  end
end

puts '----'
p.edad
p.set_edad(10)
p.edad