describe 'Stack' do
  Stack = Class.new do
    attr_accessor :current_node, :capacity

    invariant { capacity >= 0 }

    post { empty? }
    def initialize(capacity)
      @capacity = capacity
      @current_node = nil
    end

    pre { !full? }
    post { height > 0 }
    def push(element)
      @current_node = Node.new(element, current_node)
    end

    pre { !empty? }
    def pop
      element = top
      @current_node = @current_node.next_node
      element
    end

    pre { !empty? }
    def top
      current_node.element
    end

    def height
      empty? ? 0 : current_node.size
    end

    def empty?
      current_node.nil?
    end

    def full?
      height == capacity
    end

    Node = Struct.new(:element, :next_node) do
      def size
        next_node.nil? ? 1 : 1 + next_node.size
      end
    end
  end

  before(:each) do
    @stack = nil
  end

  describe 'Init' do

    it 'should raise an exception due invalid invariant' do
      negative_capacity = -1
      expect { Stack.new(negative_capacity) }.to raise_error(InvariantError)
    end

    it 'should instatiate a new Stack without getting an error' do
      valid_capacity = 30
      expect { @stack = Stack.new(valid_capacity) }.not_to raise_error
      expect(@stack.current_node.nil?).to be true
    end

  end

  describe 'Push' do

    it 'should fail push new element when the stack has zero capacity due PRE block validation' do
      zero_capacity = 0
      expect { @stack = Stack.new(zero_capacity) }.not_to raise_error
      expect { @stack.push("element") }.to raise_error(PreBlockValidationError)
    end

    it 'should fail push new element when the stack is full due PRE block validation' do
      valid_capacity = 1
      expect { @stack = Stack.new(valid_capacity) }.not_to raise_error
      expect { @stack.push("element") }.not_to raise_error
      expect { @stack.push("element2") }.to raise_error(PreBlockValidationError)
    end

    it 'should push new elements to stack without getting validation errors' do
      valid_capacity = 4
      expect { @stack = Stack.new(valid_capacity) }.not_to raise_error
      expect { @stack.push("element0") }.not_to raise_error
      expect { @stack.push("element1") }.not_to raise_error
      expect { @stack.push("element2") }.not_to raise_error
      expect { @stack.push("element3") }.not_to raise_error
      expect(@stack.height).to be > 0
    end

  end

  describe 'Pop' do

    it 'should fail pop existing element when the stack is empty due PRE block validation' do
      valid_capacity = 30
      expect { @stack = Stack.new(valid_capacity) }.not_to raise_error
      expect { @stack.pop }.to raise_error(PreBlockValidationError)
    end

    it 'should pop existing element when the stack is not empty' do
      valid_capacity = 30
      element = 'element'
      result = nil
      expect { @stack = Stack.new(valid_capacity) }.not_to raise_error
      expect { @stack.push(element) }.not_to raise_error
      expect { result = @stack.pop }.not_to raise_error
      expect(result).to be == element
    end

  end

  describe 'Top' do

    it 'should fail getting top element when the stack is empty due PRE block validation' do
      valid_capacity = 30
      expect { @stack = Stack.new(valid_capacity) }.not_to raise_error
      expect { @stack.top }.to raise_error(PreBlockValidationError)
    end

    it 'should return top element when the stack is not empty' do
      top_element = nil
      valid_capacity = 30
      element = 'element'
      expect { @stack = Stack.new(valid_capacity) }.not_to raise_error
      expect { @stack.push(element) }.not_to raise_error
      expect { top_element = @stack.top }.not_to raise_error
      expect(top_element).to be == element
    end

  end
end