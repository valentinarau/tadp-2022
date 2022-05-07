class Contrato
  @@block_before
  @@block_after

  @@wrapped_methods = []
  @@methods_blocks = []

  def self.before(&block)
    @@block_before = block
  end

  def self.after(&block)
    @@block_after = block
  end

  def self.execute_before(&block)
    block.call
  end

  def self.execute_after(&block)
    block.call
  end

  def self.method_added(name)
    if @@wrapped_methods.include?(name) # el metodo ya fue wrappeado y se está redefiniendo
      @@wrapped_methods.delete(name)
    else # el metodo se está definiendo por primera vez o redefiniendo después de wrappearlo
      @@wrapped_methods << name
      name_before = "@#{name.to_s}_before".to_sym
      @@methods_blocks << { nombre: name, before: @@block_before, after: @@block_after }
      @@block_before = nil
      @@block_after = nil
      orig_meth = instance_method(name)
      element = @@methods_blocks.select {
        |e| e[:nombre] == name
      }.first
      define_method(name) do |*args, &block|
        unless element[:before].nil?
          element[:before].call
        end
        orig_meth.bind(self).call *args, &block
        unless element[:after].nil?
          element[:after].call
        end
      end
    end
  end
end

class Persona < Contrato

  before { puts "BLOCK BEFORE" }
  after { puts "BLOCK AFTER" }

  def edad
    puts 'la edad es ' + @edad.to_s
    @edad
  end

  def set_edad(n)
    puts 'seteando edad en ' + n.to_s
    @edad = n
  end

  before { puts "another_method - BLOCK BEFORE" }
  after { puts "another_method - BLOCK AFTER" }
  def another_method
    puts "ejemplo"
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

puts '----'
p.another_method