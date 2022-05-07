class Contrato
  @@block_pre
  @@block_post

  @@invariants = []
  @@wrapped_methods = []
  @@methods_blocks = []

  def self.invariant(&block)
    @@invariants << block
  end

  def self.pre(&block)
    @@block_pre = block
  end

  def self.post(&block)
    @@block_post = block
  end

  def self.execute_before(&block)
    block.call
  end

  def self.execute_after(&block)
    block.call
  end

  def self.check_invariants
    @@invariants.each do |invariant|
      unless invariant.call
        # raise Exception
        puts "Rompe"
      end
    end
  end

  def self.method_added(name)
    if @@wrapped_methods.include?(name) # el metodo ya fue wrappeado y se está redefiniendo
      @@wrapped_methods.delete(name)
    else # el metodo se está definiendo por primera vez o redefiniendo después de wrappearlo
      @@wrapped_methods << name
      name_before = "@#{name.to_s}_before".to_sym
      @@methods_blocks << { nombre: name, pre: @@block_pre, post: @@block_post }
      @@block_pre = nil
      @@block_post = nil
      orig_meth = instance_method(name)
      element = @@methods_blocks.select {
        |e| e[:nombre] == name
      }.first
      check = method(:check_invariants)
      define_method(name) do |*args, &block|
        unless element[:pre].nil?
          element[:pre].call
        end
        orig_meth.bind(self).call *args, &block
        unless element[:post].nil?
          element[:post].call
        end
        check.call
      end
    end
  end
end

class Persona < Contrato

  pre { puts "BLOCK BEFORE" }
  post { puts "BLOCK AFTER" }
  invariant { (@edad.nil? ? 0 : @edad) > 0 }

  def edad
    puts 'la edad es ' + @edad.to_s
    @edad
  end

  def set_edad(n)
    puts 'seteando edad en ' + n.to_s
    @edad = n
  end

  pre { puts "another_method - BLOCK BEFORE" }
  post { puts "another_method - BLOCK AFTER" }
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