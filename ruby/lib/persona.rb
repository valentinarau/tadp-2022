module Contrato

  def invariant(&block)
    if @invariants.nil?
      @invariants = []
    end
    @invariants << block
  end

  def pre(&block)
    @block_pre = block
  end

  def post(&block)
    @block_post = block
  end

  def execute_before(&block)
    block.call
  end

  def execute_after(&block)
    block.call
  end

  def check_invariants
    unless @invariants.nil?
      @invariants.each do |invariant|
        unless invariant.call
          # raise Exception
          puts "Rompe invariant"
        end
      end
    end
  end
end

class Module
  include Contrato

  private def init_if_needed
    if @wrapped_methods.nil?
      @wrapped_methods = []
    end
    if @method_blocks.nil?
      @method_blocks = {}
    end
  end

  def method_added(method_name)
    init_if_needed
    if @wrapped_methods.include?(method_name) # el metodo ya fue wrappeado y se está redefiniendo
      @wrapped_methods.delete(method_name)
    else  # el metodo se está definiendo por primera vez o redefiniendo después de wrappearlo
      @wrapped_methods << method_name
      @method_blocks[method_name] = { pre: @block_pre, post:@block_post }
      @block_pre = nil
      @block_post = nil
      orig_meth = instance_method(method_name)
      method_data = @method_blocks[method_name]
      check = method(:check_invariants)
      define_method(method_name) do |*args, &block|
        unless method_data[:pre].nil?
          puts "METHOD #{method_name} PRE call"
          method_data[:pre].call
        end
        orig_meth.bind(self).call *args, &block
        unless method_data[:post].nil?
          puts "METHOD #{method_name} POST call"
          method_data[:post].call
        end
        check.call
      end
    end
  end
end

class Persona
  # invariant { !@edad.nil? && @edad > 0 }
  # invariant { false }
  invariant { true }

  pre { "".nil? }
  post { "".nil? }
  def edad
    puts 'la edad es ' + @edad.to_s
    @edad
  end

  def set_edad(n)
    puts 'seteando edad en ' + n.to_s
    @edad = n
  end

  pre { "".nil? }
  post { "".nil? }
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